#!/usr/bin/env ruby

class SAIS
  
  def initialize(input_string)
    @original_input_string = input_string
    @recursion_level = 0
  end
  
  def get_output_SA
    return @output_suffix_array
  end
  
  def calculate_suffix_array
    
    puts "\n............................"
    puts "........CALCULATING........."
    puts "............................\n\n"
    
    input_string = @original_input_string
    
    # virtually append minimum value to the end of the string
    input_string << 0
    
    # Call main method
    output = main_sais(input_string)
    
    # Output: Calculated SA (without first element
    # for appended 0 value)
    output.shift
    @output_suffix_array = output
    
    
    puts "\n............................"
    puts "...........OVER............."
    puts "............................\n\n"
    
  end
  
  
    
  # Main method for calculating suffix array
  # Nong-Zhang-Chano algorithm: SA-IS
  # Algorithm steps are split into methods
  def main_sais(input_string)
    
    start_time = Time.now
    
    @recursion_level += 1

    # string lenght
    n = input_string.size
    
    puts "-- SA_IS called --" + ">" * @recursion_level + "\n"
    print_time("Start! Calculating for #{n} chars")
    
    
    t_array, bucket_pointers =
      classify_types_determine_bucket_ptrs(input_string, n, true)
    print_time("L-S types and buckets")
    puts t_array.to_s
    puts bucket_pointers.to_s
    
    # new suffix array
    suffix_array = initialize_suffix_array(n)
    
    # p_1 array
    lms_pointers, lms_pointers_hash =
      determine_LMS_substring_pointers(t_array,
      bucket_pointers, suffix_array, input_string)
    print_time("After 1st step")  
    print "P1: "
    puts lms_pointers.to_s
    puts "SA after 1st step: "
    puts suffix_array.to_s
    puts bucket_pointers.to_s
    
    induce_SA_L(t_array, bucket_pointers, suffix_array, input_string)
    puts "SA & B(after induceSA_L): "
    puts suffix_array.to_s
    
    print_time("After Induce SA_L")
    
    
    induce_SA_S(t_array, bucket_pointers, suffix_array, input_string)
    puts "SA & B(after induceSA_S): "
    puts suffix_array.to_s
    
    print_time("After Induce SA_S")
    
    end_time = Time.now
    puts "-- SA_IS return --" + ">" * @recursion_level + "\n"
    print_time_in_seconds("Time for #{input_string.size} chars",
      (end_time-start_time))
    
    @recursion_level -= 1
    
    return suffix_array
  
  end
  
  
  def initialize_suffix_array(size)
    sa = Array.new(size, -1)
    return sa
  end
  


  def classify_types_determine_bucket_ptrs(input_string, n, set_to_end)
    # array of L- or S-type characters
    # S-type : value 1, L-type : value 0
    
    
    t_array = Array.new(n, -1)
    
    t_array[n - 1] = 1
    t_array[n - 2] = 0
    
    chars_count = Hash.new(0)
    chars_count[input_string[n - 1]] += 1
    chars_count[input_string[n - 2]] += 1
    
    (n - 3).downto(0){ |i|
      t_array[i] = ((input_string[i] < input_string[i + 1] ||
        input_string[i] == input_string[i + 1] && t_array[i + 1] == 1 )? 1 : 0)
      # calculate number of occurrences of each character in input 
      chars_count[input_string[i]] +=1
    }
    
    bucket_pointers = Hash.new(0)
    total_count = 0
    
    chars_count.sort.map do |char, count|
      total_count += count
      # This is initialization of bucket pointers
      # Set them to the end of each bucket (step 1)
      bucket_pointers[char] = (total_count - 1)

    end
    
    return t_array, bucket_pointers
  end
  
  
  def determine_buckets(input_string_array, set_to_end, *old_buckets)
    
    chars_count = Hash.new(0)
    # calculate number of occurrences of each character in input 
    input_string_array.each do |v|
      chars_count[v] +=1
    end
    
    if old_buckets.size > 0
      bucket_pointers = set_buckets_pointers(chars_count, set_to_end, old_buckets)
    else
      bucket_pointers = set_buckets_pointers(chars_count, set_to_end)
    end
    
    return bucket_pointers
    
  end
  
  
  def set_buckets_pointers(chars_count, set_to_end, *old_buckets)
    
    # set bucket pointers
    # if set_to_end is True, pointer will be on last element
    # if there is no old_buckets parameter pased, create new Hash
    # otherwise, use old buckets and set pointers to beggining/end
    if old_buckets.size == 0
      bucket_pointers = Hash.new(0)
    else
      bucket_pointers = old_buckets[0][0]
      bucket_pointers.clear

    end

    total_count = 0
    
    chars_count.sort.map do |char, count|
      total_count += count
      bucket_pointers[char] =
        (set_to_end ? (total_count - 1) : (total_count - count))

    end
    
    #puts bucket_pointers.to_s
    
    return bucket_pointers
  end
  
  
  def determine_LMS_substring_pointers(t_array, bucket_pointers,
                                       suffix_array, input_string)
    p_1 = []
    p_1_hash = {}
    hash_size = 0
    t_array.each_with_index do |type, index|
      if type == 1 && index > 0 && t_array[index - 1] == 0
        p_1 << index
        p_1_hash[index] = hash_size
        hash_size += 1
        # add LMS suffix index to SA in appropriate bucket
        # shift bucket pointer left
        # (1st step of induced sort algorithm I)
        char_value = input_string[index]
        pointer = bucket_pointers[char_value]
        bucket_pointers[char_value] -= 1
        suffix_array[pointer] = index
      end
      
    end
    return p_1, p_1_hash
  end
  
  
  def induce_SA_L(t_array, bucket_pointers,
                  suffix_array, input_string)
    # (2nd step of induced sort algorithm )
    # set bucket pointers to the FIRST element of each bucket
    #bucket_pointers = determine_buckets(input_string, false, bucket_pointers)

    bucket_pointers = determine_buckets(input_string, false, bucket_pointers)

    
    # foreach SA[i] > 0 check type
    suffix_array.each_with_index do |val, index|
      new_value = suffix_array[index]
      
      if new_value > 0
        if (t_array[new_value - 1] == 0)
          # if the type is L, add SA[i]-1 to the BEGGINING of appropriate bucket
          # shift bucket pointer RIGHT
          char_value = input_string[new_value - 1]
          pointer = bucket_pointers[char_value]
          bucket_pointers[char_value] += 1
          suffix_array[pointer] = new_value - 1
          
          #puts suffix_array.to_s
        end
      end

    end
  end
  
  
  def induce_SA_S(t_array, bucket_pointers,
                  suffix_array, input_string)
    # (3rd step of induced sort algorithm)
    # set bucket pointers to the LAST element of each bucket
    bucket_pointers = determine_buckets(input_string, true, bucket_pointers)


    # foreach SA[i] > 0 check type
    # this time, check SA from the end to the beggining
    suffix_array.reverse.each_with_index do |val, index|

      new_value = suffix_array[-(index + 1)]
      if new_value > 0
        
        if (t_array[new_value - 1] == 1)

          # if the type is S, add SA[i]-1 on the END of appropriate bucket
          # shift bucket pointer LEFT
          char_value = input_string[new_value - 1]
          pointer = bucket_pointers[char_value]
          bucket_pointers[char_value] -= 1
          suffix_array[pointer] = new_value - 1
          
          #puts suffix_array.to_s
        end
      end
    end  
    #puts bucket_pointers.to_s
  end
  
  
  
end