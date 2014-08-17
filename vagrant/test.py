
from pymongo import MongoClient
import gridfs

db = MongoClient().gridfs_example
fs = gridfs.GridFS(db)

a = fs.put("hello world")

print a

print fs.get(a).read()
