# LinuxSda
Implements a simple filesystem.
A filesystem is a record, held in memory, of the relationships between directories and files and
which disk blocks are allocated to each files. A filesystem consists of directories and files
allocated to disk blocks. Directories contain both files and directories, starting at a "root". Only
files occupy disk space, in non-contiguous 1KB increments. Filenames can contain the
characters [a-z1-9]+

*Test suite - is a container that has a set of tests which helps testers in executing and
reporting the test execution status.

Mutation
Code coverage
Junit Annotations
Redundant Test Cases


block 0 - [root,mail,inbox,file3]
block 1 - [root,docs,file1]
block 2 - [root,mail,file0]
block 3 - [root,mail,file0]
block 4 - [root,mail,file0]
block 5 - [root,mail,file0]
block 6 - [root,mail,inbox,file4]
block 7 - null (this block is free)
block 8 - [root,docs,file1]
block 9 - null (this block is free)
block 9 - [root,java,file2]
block 10 - [root,java,file2]


The methods of the
FileSystem
class are:
1. constructor
FileSystem(int m)
initializes a file system with the empty root directory and m KB of disk blocks.
2. void file(String[] name, int k)
creates the file of size k KB in the directory indicated by the file name, and allocates disk
blocks to it. If any of the directories in the file name don't exist, they are also created. If
there are not enough free blocks for the file, throws OutOfSpaceException. If the first
directory is not root, throws BadFileNameException. You do not have to check that the
file name contains only lower case alphanumeric characters. if the file already exists,
removes the old version (de-allocates disk blocks) and creates the new one. If it is a
name of an existing directory, throws BadFileNameException. if the existing file takes
less disk space than the version which is being created, and the current version causes
OutOfSpaceException, this is thrown without deleting the old version of the file.
3. void dir(String[] name)
creates an empty directory with the given name. if the directory already exists, does
nothing. If there is already a file with the same name or the name of the directory does
not start with root, throws BadFileNameException.
4. void rmfile(String[] name)
removes the file and frees the disk space. If the file does not exist does nothing.
5. void rmdir(String[] name)
removes the directory if it does not contain any files, otherwise throws
DirectoryNotEmptyException. If the directory does not exist does nothing.
6. String[] lsdir(String[] name)
returns the list of names of files and subdirectories in the directory (short names, e.g.
below lsdir([root,mail]) should return [file0,inbox]). If the directory does not exist returns
null.
7. String[][] disk()
returns an array where each index corresponds to a disk block and the entry is either
null if the block is free or an array of strings which is the name of the file which occupies
that block.
