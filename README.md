# Magic Printer
Magic the Gathering is &trade; and &copy; Wizards of the Coast, Inc, a subsidiary of Hasbro, Inc. All rights reserved.

The intent of this program is to provide a way to do the following:

1. Test out decks before buying single cards.
2. Combine fanmade cards and official cards to be used on equal footing in casual games.
3. Make copies of valuable cards to play with so that they don't get damaged.

This program is not intended to make fake Magic cards (in fact, the pictures it will print are fairly low quality).

## Running
Prerequisite: You must have a Java 7 (or newer) JRE installed. I'd suggest using the Oracle JRE, as I haven't tested any others - but others might work. All major operating systems _should_ work, but I've only tested this on Windows 7 and OSX Mountain Lion.

1. Download [printer.zip](https://github.com/forana/Deck-Printer/raw/master/dist/printer.zip)
2. Extract
3. Double-click deckprinter.jar
4. Enjoy!

If double-clicking deckprinter.jar does not work, open a terminal/command prompt window at the extracted directory and type
	java -jar deckprinter.jar
(this will also show useful output if you're encountering problems)

## Supported Deck Formats
+ MTGO (this will also let you import decks from Wizards' articles)
+ tappedout.net text export

In general, something like this will work:
	1	Some Card
	4	Some Other Card
	2	Yet Another Card

## Importing Custom Sets
A single folder containing images with the intended card directly inside it can be imported as a custom set. Example structure:
	Custom Set
	 > Card 1.jpg
	 > Card 2.jpg
	 > Card 3.jpg

Supported image formats are JPG and PNG.

## TODO
+ Allow printing of oversized (commander / archenemy / planechase / vanguard) cards at correct size.
+ Corner normalization
+ Center printed page on the paper
+ Zoom in on hover (JTooltip is ridiculously unflexible)
+ Make code readable/resolve cleanup TODOs
