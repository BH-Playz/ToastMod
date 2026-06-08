This is the source code for a failed backport to 1.19.4

Here's a few notes about this code:

1. All of the recipes likely use a 1.21.1-style structure, including the folder name being "recipe" (pluraln't). This is the reason why recipes were broken on the 1.20.1-1.20.6 backport, those versions needed "recipes" (plural)
2. This port started before 1.0.7 fully released if I had to guess
3. This code is based on the 1.20.4 port, but isn't fully ported to 1.19.4, meaning it does not build without changes.

If you want to clone this and try to port it fully, wing it. Just make sure you say "It's based on outdated code" and give credit.
