#!/usr/bin/env python
import sys

for line in sys.stdin:
        cj, mj, vj = [float(el) for el in line.split()]
        break

for line in sys.stdin:
        try:
            ck, mk, vk = [float(el) for el in line.split()]
            vi = ((cj * vj + ck * vk) / (cj + ck)) + cj * ck * ((mj - mk)/(cj + ck))**2
            cj, mj, vj = ck, mk, vi
        except:
            pass

print(vj)
