#!/usr/bin/env ruby

def t1
  c = "ATTAGCGAGCG$".unpack("c*")


  t = Array.new
  c_reversed = c.reverse
  c_reversed.each_with_index do |asci_val, index|
    
    if index == 0
      t << "S"
    elsif index == 1
      t << "L"
    else
      t << ((asci_val < c_reversed[index - 1] ||
            asci_val == c_reversed[index - 1] && t[index - 1] == "S") ? "S" : "L")
  
    end
  
  
  
  end
  
  t.reverse!
  puts t.to_s
end

def t2
  arr = [10, 15, 20, 20, 15, 20, 10, 3, 20]
  
  bucks = Hash.new(0)
  
  arr.each do |v|
    bucks[v] +=1
  end
  
  puts bucks.to_s
  
  bucks.sort.map do |char, num_of_occurr|
    puts "NOO[#{char}] = #{num_of_occurr}"

  end
  
  puts bucks.to_s
end

def t3
  arr = [5, 2, 4, 3]
  puts arr
  
  arr.map! {|x| x = 1}
  puts arr
  
  [5, 2, 4, 3].reverse.each_with_index { |val, ind| puts "#{ind}: #{val}" }
end

t3