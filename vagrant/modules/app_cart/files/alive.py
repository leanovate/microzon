#!/usr/bin/env python

import urllib2
import sys
import os.path

if os.path.isfile("/opt/cart/shutingdown"):
	print "Cart service in graceful shutdown mode"
	sys.exit(1)

try:
	urllib2.urlopen("http://localhost:9990/health")
	print "Cart service responding"
except urllib2.HTTPError as e:
	if e.code < 500:
		print "Cart service responded with %d" % e.code
		sys.exit(1)
	else:
		print "Cart service not responding"
		sys.exit(255)
except:
	print "Cart service not responding"
	sys.exit(255)

sys.exit(0)
