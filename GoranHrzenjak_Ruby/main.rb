#!/usr/bin/env ruby


$LOAD_PATH << '.'

require "parser"
require "helpers"
require "sais_methods"

puts "\n"

start_memory = report_memory
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
  end_memory = report_memory
  print_time_in_seconds("\nTime to calculate suffix array with #{input_string.size} chars", (end_time - start_time))
  puts "Memory used: #{(end_memory - start_memory)} Kb"
  suffix_array_out = sa_is.get_output_SA
  

  
  input_parser.write_output(suffix_array_out)
end

