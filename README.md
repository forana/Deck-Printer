# Magic Printer
Magic the Gathering is &trade; and &copy; Wizards of the Coast, Inc, a subsidiary of Hasbro, Inc. All rights reserved.

The intent of this program is to provide a way to do the following:

1. Test out decks before buying single cards.
2. Combine fanmade cards and official cards to be used on equal footing in casual games.
3. Make copies of valuable cards to play with so that they don't get damaged.

This program is not intended to make fake Magic cards (in fact, the pictures it will print are fairly low quality).

## Running
<b>NOTE: This program isn't really finished. Consider the current state "extreme alpha" - your results may vary wildly (though I'd like to hear about them).</b>

Prerequisite: You must have the Oracle Java 7 (or newer) JRE installed. All major operating systems _should_ work, but I've only tested this on Windows 7 and OSX Mountain Lion.

1. Download [printer.zip](dist/printer.zip)
2. Extract
3. Double-click deckprinter.jar
4. Enjoy!

If double-clicking deckprinter.jar does not work, open a terminal/command prompt window at the extracted directory and type
	java -jar deckprinter.jar
(this will also show useful output if you're encountering problems)

## Supported Deck Formats
+ MTGO (this will also let you import decks from Wizards' articles)
+ tappedout.net text export

## TODO
+ Nicer UI
+ bat/sh scripts for running jar
+ Allow to choose which image to use for each set, instead of only the highest multiverse ID.
+ Allow printing of commander-sized cards
+ Allow printing of archenemy / planechase / vanguard cards.
+ Allow import of sets from Magic Set Editor
+ Allow use of custom image sets instead of just Gatherer
+ Support more import formats
