#!/usr/bin/env ruby


$LOAD_PATH << '.'

require "parser"
require "helpers"
require "sais_methods"

puts "\n"

input_parser = Parser.new
input_parser.parse_input_args

input_parser.get_input_strings.each do |input_string|
  #puts input_string
  #puts input_string.length
  
  #convert to ASCII values

  input_string_ASCII = input_string.unpack("c*")
  
  sa_is = SAIS.new(input_string_ASCII)
  
  start_time = Time.now
  sa_is.calculate_suffix_array
  end_time = Time.now
  
  print_time_in_seconds("\nTime to calculate suffix array", (end_time - start_time))
  
  string_out = sa_is.get_output_SA
 
  for i in 0..14
    #puts "#{string_out[i]}"
  end
  
  input_parser.write_output(string_out)
end