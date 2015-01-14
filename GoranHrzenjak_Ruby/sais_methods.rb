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
    
    
    end_time = Time.now
    puts "-- SA_IS return --" + ">" * @recursion_level + "\n"
    print_time_in_seconds("Time for #{input_string.size} chars", (end_time-start_time))
    
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
  
end