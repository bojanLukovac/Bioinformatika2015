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
    
    
    # to do
    suffix_array = []
    
    
    end_time = Time.now
    puts "-- SA_IS return --" + ">" * @recursion_level + "\n"
    print_time_in_seconds("Time for #{input_string.size} chars", (end_time-start_time))
    
    @recursion_level -= 1
    
    return suffix_array
  
  end
  
  
end