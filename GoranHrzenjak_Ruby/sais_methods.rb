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
    
    
    t_array = classify_type_of_chars(input_string)
    print_time("Determined S/L types")
    puts t_array.to_s
    
    bucket_pointers = determine_buckets(input_string, true)
    puts bucket_pointers.to_s
    
    # to do
    suffix_array = []
    
    
    end_time = Time.now
    puts "-- SA_IS return --" + ">" * @recursion_level + "\n"
    print_time_in_seconds("Time for #{input_string.size} chars", (end_time-start_time))
    
    @recursion_level -= 1
    
    return suffix_array
  
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
  
end