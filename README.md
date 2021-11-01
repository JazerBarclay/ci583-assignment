# CI583 Assignment (2021-2022)
_Data Structures and Operating Systems 2nd Year Module Assignment_

In this assignment I must implement the Huffman coding. It's a very elegant and simple yet powerful compression algorithm.

The idea is to generate a binary sequence that represents each character required. This might be the English alphabet, some subset of that, or any collection of symbols.

Download or clone the Java project from the URL below.

Implement your solutions to the problems given in the README file in the top level of the project.

You must maintain the project structure as it is. If you submit individual Java source files, marks will be deducted.

The project contains unit tests which you should use to test your progress.

Note that passing the tests may not be a guarantee of correctness or of full marks for correctness.

See the slides from this module and the recommended reading for more information on hash tables, hash functions and prime numbers.

## Notes

Issued Friday 29th October 2021

Due by Thursday 13th January 2022 before 15:00

To be submitted as a single zip file with the report in the root folder

This assignment is setup for anonymous marking. To remain anonymous, omit your name and other identifying information in your assignment submission.

## Breakdown
### Implementation assessment criteria: 60% of overall mark

**Correctness** _[40 marks]_

You can test your progress towards a correct solution by running the unit tests supplied. However, a set of tests which all pass is not a guarantee of full marks in this area. Firstly, it is possible to make unit tests pass by “cooking the books”, i.e. by hard-coding certain values into the application. Secondly, you may create an implementation which “works”, i.e. which passes all tests, but which is not a true Huffman coding because you aren’t following the algorithm correctly. 

To achieve 30-40 marks your implementation will be correct in all aspects and show a thorough understanding of the theory behind these data structures and algorithms, as well as showing strong programming skills. 

To achieve 20-29 marks your implementation may pass some but not all unit tests but will show an understanding of some aspects of this problem. Methods that you weren’t able to get working may contain pseudo-code in comments, and some credit will be given for this.

If your code does not pass the unit tests and does not show an understanding of the problem you will receive fewer than 20 marks, but I will award credit for code that shows some understanding of the theory and implementation of these data structures and algorithms.

**Coding style** _[20 marks]_

To achieve these marks your code must conform to the Java style guidelines linked to on studentcentral, and show good ability with the Java programming language. That means that your code will be consistently and conventionally formatted, comments used appropriately and not over-used, variables named appropriately, and the code will be well-structured. You may have shown independent learning by using features of the language not discussed in the module to structure the code more elegantly.

**Report**

Write a report not longer than four A4 sides that includes at least two sections. In Section 1, describe the complexity of your encode and decode methods and discusses the applications for this kind of compression technique. (Note that to describe the complexity of these methods you need to consider the complexity of all methods called within them.) Comment on what you would need to add to this implementation in order to produce a fully working compression/decompression tool that stores compressed data in binary codes? 

In Section 2 of the report, discuss the application of some of the data structures and algorithms you learned about in the first part of the module in the context of operating systems. This will require independent learning and research on your part. Some of the best places to find information will be the lecture slides from this module and the books on operating systems that can be found in the library, especially Operating Systems in Depth (Doeppner) and Modern Operating Systems or Operating Systems: Design and Implementation (both by Tanenbaum et al). A few non-exhaustive examples of the applications you could discuss are the use of the stack and stack frames in assembly code, the use of a binary algorithm in the Buddy System, the “disc map” data structure in the S5FS filesystem or the use of hash functions in page tables, file systems and directory listings.

### Report assessment criteria: 40% of overall mark

**Discussion of complexity** _[20 marks]_

To achieve 15-20 marks you must show an excellent understanding of both the theory behind this data structure and of your actual implementation. You will have taken into account the complexity of all methods that are used by the encode and decode methods. You will use and demonstrate an understanding of the correct terminology regarding complexity. 5-15 marks will be awarded to work that provides less detail but in a way which is nevertheless accurate and which demonstrates an understanding of the question. 

**Data structures and _operating systems_** _[20 marks]_

To achieve 15-20 marks you will have shown some good independent research and made thorough use of the module material and/or other sources to discover several applications of data structures and algorithms in the domain of operating systems. You will have explained these in a thorough and convincing way in your own words. 5-15 marks will be awarded to work that provides less detail but in a way which is nevertheless accurate and which demonstrates an understanding of the question.


## Source Material
This assignment code is based on the [Huffman-Java](https://github.com/jimburton/huffman-java) repository by [James Burton](https://github.com/jimburton)

[Assignment Brief]() is stored and is available in this repository