#!/usr/bin/env python

import urllib2
import sys

try:
	urllib2.urlopen("http://localhost/health")
except urllib2.HTTPError as e:
	if e.code < 500:
		sys.exit(1)
	else:
		sys.exit(255)
except:
	sys.exit(255)

sys.exit(0)
