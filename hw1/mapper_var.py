#!/usr/bin/env python
import sys

our_sum = 0
cnt = 0
prices = []
for line in sys.stdin:
    try:
	    price = float(line)
	    our_sum += price
	    cnt += 1
	    prices.append(price)
    except:
        pass
mean = our_sum / cnt
var = sum([(elem - mean) ** 2 for elem in prices]) / cnt

print(cnt, mean, var)
