#!/usr/bin/env python

import json
import urllib2

categoriesUrl = "http://192.168.254.13/categories"
existingCategories = json.load(urllib2.urlopen(categoriesUrl))

print existingCategories

class Category:
    def __init__(self, name):
        self.name = name
        self.children = []

    def addChild(self, child):
        self.children.append(child)
        return self

    def eansureCreated(self, parent_id):
        existing = (filter(lambda category: category['name'] == self.name, existingCategories['categories']) or [None])[0]
        if existing:
            self.id = existing['id']
        else:
            data = json.dumps({'name':self.name, 'parent_id': parent_id})
            req = urllib2.Request(categoriesUrl, data, {'Content-Type': 'application/json'})
            created = json.load(urllib2.urlopen(req))
            self.id = created['id']
        for child in self.children:
            child.eansureCreated(self.id)

categoryTree = [
    Category("Food")
        .addChild(Category("Drink"))
        .addChild(Category("Veggies"))
        .addChild(Category("Meat")),
    Category("Computer")
        .addChild(Category("Hardware"))
        .addChild(Category("Software"))
        .addChild(Category("Games"))
]

for category in categoryTree:
    category.eansureCreated(None)
