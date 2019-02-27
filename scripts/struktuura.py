#!/bin/python
# Autors: Pēteris Paikens
import json, collections
import argparse

parser = argparse.ArgumentParser(description='Vārdnīcu JSONu ceļu apkopotājs')
parser.add_argument('filename', nargs='?', default='dati/analyzed_tezaurs.json', help='Apstrādājamā faila vārds')
args = parser.parse_args()

# filename = 'analyzed_tezaurs.json'
# filename = 'mlvv.json'
# filename = 'llvv.json'

# No http://stackoverflow.com/questions/12507206/python-recommended-way-to-walk-complex-dictionary-structures-imported-from-json
def dict_generator(indict, pre=None):
    pre = pre + ':' if pre else ''
    if isinstance(indict, dict):
        for key, value in indict.items():
            if key == 'Flags':
                yield pre + key
            elif isinstance(value, dict):
                for d in dict_generator(value, pre + key):
                    yield d
            elif isinstance(value, list) or isinstance(value, tuple):
                for v in value:
                    for d in dict_generator(v, pre + key):
                        yield d
            else:
                yield pre + key # + ':' + value
    else:
        if pre.endswith(':'):
            pre = pre[:-1]
        yield pre # + indict

with open(args.filename, encoding = 'UTF-8') as data_file:
	data = json.load(data_file)

ceļi = collections.Counter()
for nr, entry in enumerate(data):
	ceļi.update(dict_generator(entry))
	if nr > 0 and nr % 10000 == 0:
		# break
		print(nr)
		pass

for ceļš, skaits in ceļi.most_common():
	print(ceļš, skaits)