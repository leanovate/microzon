#!/usr/bin/env python

import json
import urllib2
import string
import itertools
import random
import sys

host = sys.argv[1]
categoriesUrl = "http://%s/categories" % host
productsUrl = "http://%s/products" % host

#categoriesUrl = "http://localhost:8080/categories"
#productsUrl = "http://localhost:8080/products"

initial_consonants = (set(string.ascii_lowercase) - set('aeiou')
                      # remove those easily confused with others
                      - set('qxc')
                      # add some crunchy clusters
                      | set(['bl', 'br', 'cl', 'cr', 'dr', 'fl',
                             'fr', 'gl', 'gr', 'pl', 'pr', 'sk',
                             'sl', 'sm', 'sn', 'sp', 'st', 'str',
                             'sw', 'tr'])
                      )

final_consonants = (set(string.ascii_lowercase) - set('aeiou')
                    # confusable
                    - set('qxcsj')
                    # crunchy clusters
                    | set(['ct', 'ft', 'mp', 'nd', 'ng', 'nk', 'nt',
                           'pt', 'sk', 'sp', 'ss', 'st'])
                    )

vowels = 'aeiou' # we'll keep this simple

# each syllable is consonant-vowel-consonant "pronounceable"
syllables = map(''.join, itertools.product(initial_consonants, 
                                           vowels, 
                                           final_consonants))

# you could trow in number combinations, maybe capitalized versions... 

def gibberish(wordcount, wordlist=syllables):
    return ' '.join(random.sample(wordlist, wordcount))    


existingCategories = json.load(urllib2.urlopen(categoriesUrl))

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

existingCategories = json.load(urllib2.urlopen(categoriesUrl))

print existingCategories

for category in existingCategories['categories']:
    existingProducts = json.load(urllib2.urlopen(categoriesUrl + '/' + category['id'] + '/products'))
    if len(existingProducts['activeProducts']) < 5:
        for i in range(0, 5):
            options = [ {'name':'black', 'description':gibberish(20), 'priceInCent':random.randint(100, 100000)},
                        {'name':'blue', 'description':gibberish(20), 'priceInCent':random.randint(100, 100000)},
                        {'name':'white', 'description':gibberish(20), 'priceInCent':random.randint(100, 100000)}]
            product = {'name':category['name'] + ' ' + gibberish(12), 'description': gibberish(100), 'options':options, 'images':[], 'categories': [category['id']]}
            data = json.dumps(product)
            req = urllib2.Request(productsUrl, data, {'Content-Type': 'application/json'})
            created = json.load(urllib2.urlopen(req))
            print created

