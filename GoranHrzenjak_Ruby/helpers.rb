#!/usr/bin/env ruby
class Time
  def to_milliseconds
    (self.to_f * 1000.0).to_i
  end
end

def print_time(msg)
  timeNow = Time.now
  printf "%-35s %s\n", msg, timeNow.strftime("%H:%M:%S.#{timeNow.to_milliseconds % 1000}")
end

def print_time_in_seconds(msg, time)
  printf "%s : %5.3f s\n", msg, time
end

def report_memory
  mem = `ps ax -o pid,rss | grep -E "^[[:space:]]*#{$$}"`
          .strip.split.map(&:to_i)[1]
  puts "Memory #{mem} KB"
  return mem
end
