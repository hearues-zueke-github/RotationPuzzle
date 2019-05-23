#! /usr/bin/python3

# -*- coding: utf-8 -*-

import os
import sys

PATH_ROOT_DIR = os.path.abspath(os.path.dirname(sys.argv[0]))+"/"

if __name__ == "__main__":
    fout = open('SolvingInputs.java', 'w')

    fout.write("package net.pimathclanguage.rotationpuzzle;\n\n")
    fout.write("import java.util.HashMap;\n\n")
    fout.write("class SolvingInputs {\n")
    fout.write("  public HashMap<Integer, String> jsonInputStrings = new HashMap<>();\n\n")
    fout.write("  SolvingInputs() {\n")

    for s in range(4, 14):
        # s = 5
        file_name_template = 'field_moving_table_{s}x{s}.json'
        f = open(file_name_template.format(s=s), 'r')
        a = f.read().replace(" ", "").replace("\n", "").encode('utf-8')
        f.close()
        hex_string = a.hex().upper()

        print("Saving solve for field size {s}x{s}!".format(s=s))
        # fout.write('  String solve_field_{s}x{s} = "{content}";\n'.format(s=s, content=hex_string))
        line = '    jsonInputStrings.put({s}, String.join("", new String[]{{{content}}}));\n'.format(s=s, content=
            ", ".join(["\""+hex_string[65534*i:65534*(i+1)]+"\"" for i in range(0, len(hex_string)//65534+(0 if len(hex_string)%65534 == 0 else 1))]))
        fout.write(line)
        # fout.write('    jsonInputStrings.put({s}, "{content}");\n'.format(s=s, content=hex_string))

    fout.write("  }\n")
    fout.write("}\n")

    fout.close()
