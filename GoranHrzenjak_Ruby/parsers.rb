#!/usr/bin/env ruby

class FastaParser
  def parse
    $/ = ">"
    ARGF.gets
    while rec = ARGF.gets
      rec.chomp!
      nl = rec.index("\n")
      header = rec[0..nl-1]
      seq = rec[nl+1..-1]
      seq.gsub!(/\n/,'')
      puts [header, seq.length].join(" ")
    end
    
    return seq
  end
end