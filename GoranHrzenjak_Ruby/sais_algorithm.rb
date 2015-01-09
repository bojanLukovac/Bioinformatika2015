#!/usr/bin/env ruby

class SAIS
  
  @original_input_string
  
  def initialize(input_string)
    @original_input_string = input_string
  end
  
  def calculate_suffix_array
    main_sais(@original_input_string)
  end
  
  def main_sais(input_string)
    
    t_array = classify_type_of_chars(input_string)
    
    bucket_pointers = determine_buckets(input_string, true)
    
    # new suffix array
    suffix_array = initialize_suffix_array(input_string.size)
    puts suffix_array.to_s
    
    # p_1 array
    lms_pointers = determine_LMS_substring_pointers(t_array, bucket_pointers,
                                                  suffix_array, input_string)
    puts bucket_pointers.to_s
    puts suffix_array.to_s
    
    puts "***"
    
    induce_SA_L(t_array, bucket_pointers, suffix_array, input_string)
    puts "***"
    puts bucket_pointers.to_s
    puts suffix_array.to_s
    
    induce_SA_S(t_array, bucket_pointers, suffix_array, input_string)
    puts "***"
    puts bucket_pointers.to_s
    puts suffix_array.to_s
    
  end
  
  def initialize_suffix_array(size)
    sa = Array.new(size, -1)
    return sa
  end
  
  
  def classify_type_of_chars(input_string)
    # array of L- or S-type characters
    # S-type : value 1, L-type : value 0
    t_array = Array.new
    
    str_reversed = input_string.reverse
    str_reversed.each_with_index do |ascii_val, index|
      
      if index == 0
        # the last suffix (char) is always S-type
        t_array << 1
      elsif index == 1
        t_array << 0 
      else
        t_array << ((ascii_val < str_reversed[index - 1] ||
              ascii_val == str_reversed[index - 1] && t_array[index - 1] == 1) ? 1 : 0)
    
      end

    end
    t_array.reverse!
    return t_array
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
    # if new_buckets is True, create new Hash, othervise just clear
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
    t_array.each_with_index do |type, index|
      if type == 1 && index > 0 && t_array[index - 1] == 0
        p_1 << index
        
        # add LMS suffix index to SA in appropriate bucket
        # shift bucket pointer left
        # (1st step of induced sort algorithm I)
        char_value = input_string[index]
        pointer = bucket_pointers[char_value]
        bucket_pointers[char_value] -= 1
        suffix_array[pointer] = index
      end
      
    end
    return p_1
  end
  
  
  def induce_SA_L(t_array, bucket_pointers,
                  suffix_array, input_string)
    # (2nd step of induced sort algorithm I)
    # set bucket pointers to the FIRST element of each bucket
    bucket_pointers = determine_buckets(input_string, false, bucket_pointers)

    # foreach SA[i] > 0 check type
    suffix_array.each_with_index do |value, index|
      if value > 0
        if (t_array[value-1] == 0)
          # if type is L, add SA[i]-1 to the beggining of appropriate bucket
          # shift bucket pointer right
          char_value = input_string[value-1]
          pointer = bucket_pointers[char_value]
          bucket_pointers[char_value] += 1
          suffix_array[pointer] = value-1
          
          #puts suffix_array.to_s
        end
      end
    end  
    
  end
  
  
  def induce_SA_S(t_array, bucket_pointers,
                  suffix_array, input_string)
    # (3rd step of induced sort algorithm I)
    # set bucket pointers to the LAST element of each bucket
    bucket_pointers = determine_buckets(input_string, true, bucket_pointers)

    # foreach SA[i] > 0 check type
    suffix_array.to_enum.with_index.reverse_each do |value, index|
      if value > 0
        puts "i: #{index}"
        #if (t_array[value-1] == 0)
        #  puts "L value"
        #   if type is L, add SA[i]-1 on the beggining of appropriate bucket
        #   shift bucket pointer right
        #  char_value = input_string[value-1]
        #  pointer = bucket_pointers[char_value]
        #  bucket_pointers[char_value] += 1
        #  suffix_array[pointer] = value-1
        #  
        #  puts suffix_array.to_s
        #end
      end
    end  
    
  end
  
  
end

