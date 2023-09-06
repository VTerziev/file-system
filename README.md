# file-system
# Architectural overview:
 The most important unit in this File System is a block. Every block represents a chunk of the stored information
about a file. There are 2 types of blocks: data and metadata blocks. The data blocks contain the content of the file
itself, while the metadata blocks contain, as the name suggests, metadata. The latter could store information like name
of the file, modification timestamps, access rights, etc. It also stores the location of the data blocks, belonging to
the file.

 There are a fixed number of existing data blocks and metadata blocks in a File System. That limits the maximal
number of files that can be stored. The File System splits the existing storage in 5 segments:
- One for storing general information about the FileSystem. (Could contain checksums and other such properties.
  Currently only stores the root folder id).
- One for keeping track of which metadata blocks are in use.
- One for keeping track of which data blocks are in use.
- One for keeping the metadata blocks themselves
- One for keeping the data blocks.

  For very large files, a single metadata block may not be enough to store all of the data blocks. That's why in
  these cases, the first metadata block can contain pointers to other metadata blocks, extending the possible data block
  pointers. Even so, the metadata block structure is fixed and therefore, there is a limit for the maximal size of a file.

# Notable TODOs in the project:
- Use dependency injection for constructing objects
- Use mocks for tests
- Use more checked exceptions instead of unchecked.