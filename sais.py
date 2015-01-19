from collections import defaultdict
import time

class sais:

	def __init__ (self):
		self.original_input_string = ""
		self.recursion_level = 0
		self.output_suffix_array = []
		self.trace = False
		
	def initialize(self, input_string):
		self.original_input_string = input_string
		self.recursion_level = 0
		
	def set_trace(self, _trace):
		self.trace = _trace
		
	def get_output_SA(self):
		return self.output_suffix_array
	
	def calculate_suffix_array(self):
    
		print ("\n............................")
		print ("........CALCULATING.........")
		print ("............................\n\n")
		
		input_string = self.original_input_string
		
		# virtually append sentinel value to the end of the string
		input_string += "$"
		
		# Begin timing
		start_time = time.time()
		# Call main method
		output = self.main_sais(input_string)
		end_time = time.time()
		print ("Time for %(len)d chars: %(duration).3g seconds" % {"len" : len(input_string), "duration" : end_time-start_time})

		#print ("nakon returna iz main, output varijabla ")
		#self.log_var("output", output)

		
		# Output: Calculated SA (without first element
		# for appended 0 value)
		self.output_suffix_array = output[1:]
		#print ("postavljena instance varijabla")
		#self.log_var("self.output_suffix_array", self.output_suffix_array)
		
		
		print ("\n............................")
		print ("...........OVER.............")
		print ("............................\n\n")

		
	
	
	
	
	# Main method for calculating suffix array
	# Nong-Zhang-Chano algorithm: SA-IS
	# Algorithm steps are split into methods
	def main_sais(self, input_string):
    
		#print ("input_string=", input_string)
	
		#start_time = time.time()
    
		self.recursion_level += 1

		# string lenght
		n = len(input_string)
    
		print ("-- SA_IS called --" + ">" * self.recursion_level + "\n")
		if self.trace:
			#print_time("Start! Calculating for #{n} chars")
			print(),
		else:
			print (".", end=""),
    
		t_array, bucket_pointers, buckets_beginnings_ends = self.classify_types_determine_bucket_ptrs(input_string, n, True)
		if self.trace:
			#print_time("L-S types and buckets")
			print (t_array)
			print (bucket_pointers)
			print (buckets_beginnings_ends)
		else:
			print (".", end=""),
    
		# new suffix array
		suffix_array = self.initialize_suffix_array(n)
    
		# p_1 array
		lms_pointers = self.determine_LMS_substring_pointers(t_array, bucket_pointers, suffix_array, input_string)
		if self.trace:
			#print_time("After 1st step")  
			print ("P1:"),
			print (lms_pointers)
			print ("SA after 1st step:"),
			print (suffix_array)
			print ("BP after 1st step:"),
			print (bucket_pointers)
		else:
			print (".", end=""),
    
		self.induce_SA_L(t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string)
		if self.trace:
			#print_time("After Induce SA_L")
			print ("SA & B(after induceSA_L):"),
			print (suffix_array)
			print (bucket_pointers)
		else:
			print (".", end=""),
    
		self.induce_SA_S(t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string)
		if self.trace:
			#print_time("After Induce SA_S")
			print ("SA & B(after induceSA_S): ")
			print (suffix_array)
			print (bucket_pointers)
		else:
			print (".", end=""),
    
		#substring names S1
		unique_names, substring_names = self.name_LMS_substring(lms_pointers, suffix_array, t_array, input_string)
		print (".", end=""),
		if self.trace:
			#print_time("After naming")
			print ("Unique names of LMS substrings in S1:", end=""),
			print (unique_names)
			print ("S1:"),
			print (substring_names)
		else:
			print (".")
		
		if unique_names:
			if self.trace:
				print ("directly computing SA1 from SA1")
			suffix_array_short = self.directly_compute_shortened_SA(substring_names)
		else:
			if self.trace:
				print ("recursion")
			suffix_array_short = self.main_sais(substring_names)
			if self.trace:
				print ("returned from recursion")
		if self.trace:
			print ("SA1:"),
			print (suffix_array_short)
		else:
			print (".", end=""),
    

		# Induce SA from SA1
		suffix_array = self.induce_final_suffix_array(t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string, suffix_array_short, lms_pointers)
		print (".")
		
		#end_time = time.time()
		print ("-- SA_IS return --" + ">" * self.recursion_level + "\n")
		#print ("Time for %(len)d chars: %(duration).3g seconds" % {"len" : n, "duration" : end_time-start_time})
		
		self.recursion_level -= 1
		
		#print ("prije returna iz main")
		#self.log_var("suffix_array", suffix_array)

		return suffix_array

		
	def initialize_suffix_array(self, size):
		sa = [-1] * size
		return sa
	

	def classify_types_determine_bucket_ptrs(self, input_string, n, set_to_end):
		# array of L- or S-type characters
		# S-type : value 1, L-type : value 0
		
		t_array = [-1] * n
		
		t_array[n - 1] = 1
		t_array[n - 2] = 0
		
		chars_count = defaultdict(lambda: 0)
		chars_count[input_string[n - 1]] += 1
		chars_count[input_string[n - 2]] += 1
		
		for i in range (n-3, -1, -1):
			t_array[i] = 1 if ((input_string[i] < input_string[i + 1]) or (input_string[i] == input_string[i + 1] and t_array[i + 1] == 1 )) else 0
			# calculate number of occurrences of each character in input 
			chars_count[input_string[i]] +=1
		
		bucket_pointers = defaultdict(lambda: 0)
		
		# For each bucket, set beggining and end pointer
		# This saves some time for later resets of pointers
		buckets_beginnings_ends = {}
		
		total_count = 0
		
		for char in sorted(chars_count):
			count = chars_count[char]
			total_count += count
			# This is initialization of bucket pointers
			# Set them to the end of each bucket (step 1)
			bucket_pointers[char] = (total_count - 1)
			buckets_beginnings_ends[char] = {"B":(total_count - count), "E":(total_count - 1)}
		
		return t_array, bucket_pointers, buckets_beginnings_ends
	
	def reset_bucket_pointers(self, bucket_pointers, bucks_defaults, set_to_end):
		# bucks_defaults = beginnings and ends of buckets
		# set pointers to end or beginning for each bucket
		for key in bucket_pointers:
			bucket_pointers[key] = bucks_defaults[key]["E"] if set_to_end else bucks_defaults[key]["B"]

			
	def determine_buckets(self, input_string_array, set_to_end, *old_buckets):
    
		chars_count = defaultdict(lambda: 0)
		# calculate number of occurrences of each character in input 
		for v in input_string_array:
			chars_count[v] +=1
    
		if len(old_buckets) > 0:
			bucket_pointers = set_buckets_pointers(chars_count, set_to_end, old_buckets)
		else:
			bucket_pointers = set_buckets_pointers(chars_count, set_to_end)
    
		return bucket_pointers

  
	def set_buckets_pointers(self, chars_count, set_to_end, *old_buckets):
    
		# set bucket pointers
		# if set_to_end is True, pointer will be on last element
		# if there is no old_buckets parameter pased, create new Hash
		# otherwise, use old buckets and set pointers to beggining/end
		if len(old_buckets) == 0:
			bucket_pointers = defaultdict(lambda: 0)
		else:
			bucket_pointers = old_buckets[0][0]
			bucket_pointers.clear()

		total_count = 0
		
		for char in sorted(chars_count):
			count = chars_count[char]
			total_count += count
			bucket_pointers[char] =	(total_count - 1) if set_to_end else (total_count - count)

		return bucket_pointers
	

	
	def determine_LMS_substring_pointers(self, t_array, bucket_pointers, suffix_array, input_string):
    
		p_1 = []
     
		for idx in range (1,len(t_array)):
			type = t_array[idx]
      
			if type == 1 and t_array[idx - 1] == 0:
				p_1.append(idx)

				# add LMS suffix index to SA in appropriate bucket
				# shift bucket pointer left
				# (1st step of induced sort algorithm I)
				char_value = input_string[idx]
				pointer = bucket_pointers[char_value]
				bucket_pointers[char_value] -= 1
				suffix_array[pointer] = idx
			
		return p_1


	def induce_SA_L(self, t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string):
		# (2nd step of induced sort algorithm )

		# set bucket pointers to the FIRST element of each bucket
		self.reset_bucket_pointers(bucket_pointers, buckets_beginnings_ends, False)

		# foreach SA[i] > 0 check type
		for index, val in enumerate (suffix_array):
			new_value = suffix_array[index]
		  
			if new_value > 0:
				if (t_array[new_value - 1] == 0):
					# if the type is L, add SA[i]-1 to the BEGGINING of appropriate bucket
					# shift bucket pointer RIGHT
					char_value = input_string[new_value - 1]
					pointer = bucket_pointers[char_value]
					bucket_pointers[char_value] += 1
					suffix_array[pointer] = new_value - 1
					#print (suffix_array)


	def induce_SA_S(self, t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string):
		# (3rd step of induced sort algorithm)
		# set bucket pointers to the LAST element of each bucket
		self.reset_bucket_pointers(bucket_pointers, buckets_beginnings_ends, True)

		# foreach SA[i] > 0 check type
		# this time, check SA from the end to the beggining
		for index, val in enumerate(reversed(suffix_array)):

			new_value = suffix_array[-(index + 1)]
			if new_value > 0:
			
				if (t_array[new_value - 1] == 1):

					# if the type is S, add SA[i]-1 on the END of appropriate bucket
					# shift bucket pointer LEFT
					char_value = input_string[new_value - 1]
					pointer = bucket_pointers[char_value]
					bucket_pointers[char_value] -= 1
					suffix_array[pointer] = new_value - 1
					#print (suffix_array)
		#print (bucket_pointers)


	def name_LMS_substring(self, lms_pointers, suffix_array, t_array, input_string):

		#print ("*mark*")
		#print ("lms_pointers=", lms_pointers)
		#print ("suffix_array=", suffix_array)
		#print ("t_array=", t_array)
		#print ("input_string=", input_string)
	
		# True only if each character in S_1 is unique
		each_char_unique = True
    
		# S_1
		substring_names = [None] * len(lms_pointers)
		lms_pointers_hash = {}
		for index,val in enumerate(lms_pointers):
			lms_pointers_hash[val] = index
		names_count = 0
		previous_substring_index = -1
  
		n = len(suffix_array)

		substring_index = lms_pointers_hash.get(suffix_array[0])
  
		if substring_index!=None:
			substring_names[substring_index] = names_count
			previous_substring_index = substring_index
 
		for idx in range(1,n):
      
			value = suffix_array[idx]
			substring_index = lms_pointers_hash.get(value)

			#puts "i: #{idx}, val: #{value}, substring_idx: #{substring_index.to_s}"
			if substring_index != None:
			
				# Are current and previous LMS substrings equal?
			
				##
				if substring_index == (len(lms_pointers) - 1):
					current_substring = input_string[value]
					current_substring_type = t_array[value]
					#self.log_var ("1current_substring", current_substring)
					#self.log_var ("1current_substring_type", current_substring_type)
				else:
					current_substring = input_string[value:lms_pointers[substring_index + 1]+1]
					current_substring_type = t_array[value:lms_pointers[substring_index + 1]+1]
					#self.log_var ("2current_substring", current_substring)
					#self.log_var ("2current_substring_type", current_substring_type)
			
				if previous_substring_index == (len(lms_pointers) - 1):
					previous_substring = input_string[lms_pointers[previous_substring_index]]
					previous_substring_type = t_array[lms_pointers[previous_substring_index]]
					#self.log_var ("3previous_substring", previous_substring)
					#self.log_var ("3previous_substring_type", previous_substring_type)
				else:
					previous_substring = input_string[lms_pointers[previous_substring_index]:lms_pointers[previous_substring_index + 1]+1]
					previous_substring_type = t_array[lms_pointers[previous_substring_index]:lms_pointers[previous_substring_index + 1]+1]
					#self.log_var ("4previous_substring", previous_substring)
					#self.log_var ("4previous_substring_type", previous_substring_type)
			
			
				# Check lenght, compare all characters and
				# type of every character
				#if (len(current_substring) == len(previous_substring) and current_substring==previous_substring and current_substring_type==previous_substring_type):
				if (current_substring==previous_substring and current_substring_type==previous_substring_type):
					each_char_unique = False
				else:
					# NOT equal LMS substrings
					names_count += 1
			
				substring_names[substring_index] = names_count
				previous_substring_index = substring_index
				#self.log_var ("5substring_names", substring_names)
				#self.log_var ("5previous_substring_index", previous_substring_index)

		return each_char_unique, substring_names

	def log_var(self, name, var):
		print (name, "=", var)
	
		
	def directly_compute_shortened_SA(self, substring_names):
		#SA_1
		suffix_array_short = [-1] * len(substring_names)
		for index, value in enumerate (substring_names):
			suffix_array_short[value] = index
		return suffix_array_short
	
	
	def induce_final_suffix_array(self, t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string, suffix_array_short, lms_pointers):
		#self.log_var("t_array", t_array)
		#self.log_var("bucket_pointers", bucket_pointers)
		#self.log_var("buckets_beginnings_ends", buckets_beginnings_ends)
		#self.log_var("suffix_array", suffix_array)
		#self.log_var("input_string", input_string)
		#self.log_var("suffix_array_short", suffix_array_short)
		#self.log_var("lms_pointers", lms_pointers)
		#print ("Induce SA from SA1")
		suffix_array = [-1] * len(suffix_array)

		# set bucket pointers to the LAST element of each bucket
		self.reset_bucket_pointers(bucket_pointers, buckets_beginnings_ends, True)


		for idx in range(len(suffix_array_short)-1,-1,-1):
			char_position = lms_pointers[suffix_array_short[idx]]
			pointer = bucket_pointers[input_string[char_position]]
			bucket_pointers[input_string[char_position]] -= 1
			suffix_array[pointer] = char_position  

		#print ("prije induce_SA_L")
		#self.log_var("suffix_array", suffix_array)
		self.induce_SA_L(t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string)
  
		#print ("prije induce_SA_S")
		#self.log_var("suffix_array", suffix_array)
		self.induce_SA_S(t_array, bucket_pointers, buckets_beginnings_ends, suffix_array, input_string)
		#print ("poslije induce_SA_S")
		#self.log_var("suffix_array", suffix_array)
		
		return suffix_array
