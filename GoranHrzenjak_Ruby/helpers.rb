#!/usr/bin/env ruby
class Time
  def to_milliseconds
    (self.to_f * 1000.0).to_i
  end
end

def print_time(msg)
  timeNow = Time.now
  printf "%-25s %s\n", msg, timeNow.strftime("%H:%M:%S.#{timeNow.to_milliseconds % 1000}")
end