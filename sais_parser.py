import sys
import os.path

class sais_parser:

	def __init__ (self):
		self.input_strings = []
		self.input_file_name = ""
		self.output_file_name = ""

		# 0 - regular string
		# 1 - FASTA format
		self.input_file_format = 0

		# If reading FASTA format, there could be several inputs
		# Write outputs to single file
		self.file_write_mod = 'w'

		
	def print_values(self):
		print ("Time for %(len)d chars: %(duration).3g seconds" % {"len" : n, "duration" : end_time-start_time})
		print ("input_filename: %(input_file_name)s" % {"input_file_name" : self.input_file_name})
		print ("output_filename: %(output_file_name)s" % {"output_file_name" : self.output_file_name})
		print ("input_file_format: %(input_file_format}s" % {"input_file_format" : str(self.input_file_format)})

		
	def parse_input_args(self):
    
		# checking the number of passed command line arguments
		# expected 2 or 3
		if len(sys.argv) < 2:
			print ("Not enough input arguments!"),
			print ("1st arg: Input file"),
			print ("2nd arg (optional): Input file format (0 - regular string [DEFAULT], 1 - FASTA)"),
			print ("3rd arg (optional): Output file")
			print ("Usage: main inputFastaFile.fa 1 output.txt")
			sys.exit()
    
		# our input filename should be the first command line arg
		self.input_file_name = sys.argv[1]
    
		# validate input data file
		if not os.path.isfile(self.input_file_name):
			print ("Invalid 1st argument: %(input_file_name)s" % {"input_file_name" : self.input_file_name} ),
			print ("1st arg: Input file must be valid file")
			sys.exit()

		# check if there is the second argument - input file format flag
		# 0 for regular string, 1 for FASTA format
		if (len(sys.argv) > 2):
			strFormat = sys.argv[2]
			if not ( strFormat == "0" or strFormat == "1" ):
				print ("Invalid 2nd argument: %(input_file_format)s" % {"input_file_format" : sys.argv[2]}),
				print ("2nd arg (optional): Input file format (0 - regular string [DEFAULT], 1 - FASTA)")
				sys.exit()
			self.input_file_format = int(sys.argv[2])

    
		# our output filename should be the third command line arg
		# this is optional - if there is no arg passed,
		# output filename should be [INPUT_FILENAME]_output.txt
		if (len(sys.argv) > 3):
			self.output_file_name = sys.argv[3]
		else:
			base_name = os.path.splitext(os.path.split(self.input_file_name)[1])[0]
			self.output_file_name = ("%(basename)s_output.txt" % {"basename" : base_name})
    

	def get_input_strings(self):
    
		input_strings = []
		input_data_file = open(self.input_file_name, "r")
    
		# for now we only support regular string format
		self.input_file_format = 0
	
		if self.input_file_format == 0:
			print ("Reading regular string"),
			data = ""

			for line in input_data_file:
				data += line.strip()
			input_strings.append (data)
		else:
		  print ("Unknown input file format")
		  sys.exit()
    
		input_data_file.close()
    
		return input_strings

	
	def write_output(self, suffix_array):
		#if !(Dir.exists?("output"))
		#  Dir.mkdir("output")
		#end
		output_file = open(self.output_file_name, self.file_write_mod)
		output_file.write(str(suffix_array))
		output_file.write("\n")
		output_file.close()
		print ("Output writen to ", self.output_file_name)
		if self.input_file_format == 1:
			self.file_write_mod = 'a'
    
	
	def get_output_file_name(self):
		return self.output_file_name
