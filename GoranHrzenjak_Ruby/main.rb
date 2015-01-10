#!/usr/bin/env ruby

$LOAD_PATH << '.'
require "sais_methods"
require "parser"


input_parser = Parser.new
input_parser.parse_input_args

input_parser.get_input_strings.each do |input_string|
  #puts input_string
  #puts input_string.length
  
  #convert to ASCII values

  
  input_string_ASCII = input_string[0..1_000_000].unpack("c*")

  sa_is = SAIS.new(input_string_ASCII)

  start = Time.now
  sa_is.calculate_suffix_array
  stop = Time.now
  
  time_elapsed = (stop - start)*1_000
  
  puts "Time to calculate suffix array: #{time_elapsed} ms"
  
  string_out = sa_is.get_output_SA
  
  #for i in 0..14
  #  puts "#{string_out}"
  #end
  
  puts "#{string_out}"
  
end
