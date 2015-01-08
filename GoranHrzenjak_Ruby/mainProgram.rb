#!/usr/bin/env ruby

puts ARGV.length

if ARGV.length > 0
  if (File.file?(ARGV[0]))
    inputDataFile = File.open(ARGV[0], "r")
  else
    err_msg = "First input argument should be "\
      "a valid file to read from!"
    abort(err_msg)
  end
  
else
  err_msg = "Not enough input arguments!\n"
  err_msg += "1st arg: Input file, 2nd arg (optional): Output file"
  abort(err_msg)
end


inputDataFile.close()