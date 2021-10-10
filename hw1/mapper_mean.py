#!/usr/bin/env python
import sys

our_sum, cnt = 0, 0
for line in sys.stdin:
    try:
        our_sum += float(line)
        cnt += 1
    except:
        pass
ans_mean = out_sum / cnt

print(cnt, ans_mean)
