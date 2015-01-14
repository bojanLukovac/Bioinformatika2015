#!/usr/bin/env ruby
class Parser
  
  def initialize
    @input_strings = []
    @input_file_name = ""
    @output_file_name = ""
    
    # 0 - regular string
    # 1 - FASTA format
    @input_file_format = 0
    
    # If reading FASTA format, there could be several inputs
    # Write outputs to single file
    @file_write_mod = 'w'
  end
  
  def print_values
    puts "input_filename: #{@input_file_name}"
    puts "output_filename: #{@output_file_name}"
    puts "input_file_format: #{@input_file_format}"
  end
  
  
  def parse_input_args
    
    # checking the number of passed command line arguments
    # expected 2 or 3
    unless ARGV.length > 0
      puts "Not enough input arguments!"
      puts "1st arg: Input file"
      puts "2nd arg (optional): Input file format (0 - regular string [DEFAULT], 1 - FASTA)"
      puts "3rd arg (optional): Output file\n"
      puts "Usage: main inputFastaFile.fa 1 output.txt"
      exit
    end
    
    # our input filename should be the first command line arg
    @input_file_name = ARGV[0]
    
    # validate input data file
    unless File.file?(@input_file_name)
      puts "Invalid 1st argument: #{@input_file_name}"
      puts "1st arg: Input file must be valid file"
      exit
    end

    # our output filename should be the third command line arg
    # this is optional - if there is no arg passed,
    # output filename should be [INPUT_FILENAME]_output.txt
    if (ARGV.length > 2)
      @output_file_name = "output\\#{ARGV[2]}"
    else
      @output_file_name =
      "output\\#{File.basename(@input_file_name, File.extname(@input_file_name))}_output.txt" 
    end
    
    # check if there is the second argument - input file format flag
    # 0 for regular string, 1 for FASTA format
    if (ARGV.length > 1)
      @input_file_format = ARGV[1].to_i
      unless @input_file_format == 0 || @input_file_format == 1
        puts "Invalid 2nd argument: #{@input_file_format}"
        puts "2nd arg (optional): Input file format (0 - regular string [DEFAULT], 1 - FASTA)\n"
        exit
      end
    end
    
  end
  
  
  def get_input_strings
    
    input_strings = []
    input_data_file = File.open(@input_file_name, "r")
    
    if @input_file_format == 0
      puts "Reading regular string"
      input_strings << input_data_file.gets

    elsif @input_file_format == 1
      puts "Reading FASTA format"
      
      $/ = ">"
      input_data_file.gets
      while rec = input_data_file.gets
        rec.chomp!
        nl = rec.index("\n")
        #header = rec[0..nl-1]
        sequence_string = rec[(nl + 1)..-1]
        sequence_string.gsub!(/\n/, '')
        input_strings << sequence_string
        #puts [header, sequence_string.length].join(" ")
      end
      
    else
      puts "Unknown input file format: #{@input_file_format}"
      exit
    end
    
    input_data_file.close
    
    return input_strings
  end
  
  
  def write_output(suffix_array)
    File.open(@output_file_name, @file_write_mod) { |file| file.write("#{suffix_array}\n") }
    puts "Output writen to #{@output_file_name}"
    if @input_file_format == 1
      @file_write_mod = 'a'
    end
    
  end
  
  
  def get_output_file_name
    return @output_file_name
  end
  
end