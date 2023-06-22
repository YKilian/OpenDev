# JNotes

This README provides instructions on how to utilize the program JNotes.

## Installations

In order to use JNotes, it is necessary to install Java. There is no requirement to install any additional extensions.

## Setup

No setup is required.

If you wish to modify the destination path for exported files, you can do so within the Export class.

## Usage

To utilize the program, execute gui.java located at java\notes\src\main\java\src\main\java\gui\gui.java.
or start JNotes.exe

### Creating a New Note
To create a new note, click the '+ Add new note' button, which will generate a new file.

### Edit a File
To edit an existing file, simply select it from the list on the left and proceed with typing.

### Save Changes
To save your modifications, press "control+s" or click on "File" and select "Save".

### Deleting
To delete a note, press the delete key on your keyboard or click on "File" and choose "Delete".

### Exporting
You can export your notes as a .txt document. Click on "File" and select "Export as .txt". The exported file can be found in the program's export folder (java\notes\exports).

### Undo
You can undo your previous 20 editing actions by pressing "control+z".

## Alpha Features
### HTML-File-Preview
If you desire to customize your file preview using HTML, include the following line at the beginning of your code: '###Enable JNotes++###'. This will activate the advanced file preview.

### Break file preview
If you wish to interrupt the file preview before the system automatically does so, type '<' followed by any letter or word of your choice.

#### Example:
```
 Previewbreak <here
 This won't be shown in the preview
```
This will instruct the system to halt the preview after encountering the word 'Previewbreaker'.

