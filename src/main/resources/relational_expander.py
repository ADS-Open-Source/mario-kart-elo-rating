#!/usr/bin/env python3

import re
from itertools import zip_longest

# not nearly as useful as it could be
SCHEMA = "PUBLIC"
TABLE = "PLAYERS"
INITIAL_FORMAT = ['UUID', 'UUID', 'VARCHAR', 'VARCHAR', 'BOOLEAN', 'INTEGER', 'INTEGER', 'TIMESTAMP']
TARGET_FORMAT = ['UUID', 'UUID', 'VARCHAR', 'VARCHAR', 'BOOLEAN', 'INTEGER', 'INTEGER', 'TIMESTAMP', 'VARCHAR']

INPUT_FILE = 'mlekoDataTest.sql'
OUTPUT_FILE = 'mlekoDataTest.out.sql'

with open('mkeloDataTest.sql', 'r') as fin, open('mkeloDataTest.out.sql', 'w') as fout:
    zipped = zip_longest(INITIAL_FORMAT, TARGET_FORMAT)
    differences = [(index, second) for index, (first, second) in enumerate(zipped) if first != second]
    for line in fin:
        # noinspection SqlNoDataSourceInspection
        insert_value = re.search(f'INSERT INTO "{SCHEMA}"\."{TABLE}" VALUES\((.*)\);.?', line.strip())
        if insert_value:
            processed = [v.strip() for v in insert_value.group(1).split(', ')]
            for (index, data_type) in differences:
                processed.insert(index, 'NULL')
                # noinspection SqlNoDataSourceInspection
                fout.write(f'INSERT INTO "{SCHEMA}"."{TABLE}" VALUES({", ".join(processed)});\n')
        else:
            fout.write(line)
