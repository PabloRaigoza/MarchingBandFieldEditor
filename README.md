# MarchingBandFieldEditor

This is a editor to allow user to create their very own, unique field shows.
I have not included the application for windows64 because it is simply too big for me to upload.
One important thing to note is that the program requires the user to manually save the current state of the sets. This is done by pressing s.

Please email me if there are ANY bugs and I will update the source code as fast as possible.

There are a couple of key inputs in order to use this program. They are listed as follows:
  
  s -- save
    -> saves the current set that you are on.
    
  i -- inputMode
    -> inside of inputMode you can add players.
  
  d -- deleteMode
    -> inside of deleteMode you can can delete players.
  
  b -- assignBeats
    -> This will prompt you for the number the players have to walk from one spot to another. Pressing 'SPACE' assigns this number to all the players on the set.
  
  p -- makePhoto
    -> when you press p the program will make a .png image inside of your 'SHOWNAME'+Pictures folder

In a addition to this, users can do the following:

  1. Right click player
    -> A prompt will appear at the bottom of the page saying "RECORDDING". The program will record you input and when you are done recording a message you MUST PRESS 'SPACE'. This will assign your message to the player on the field, thus giving him a name.
  
  2. Drag player name
    -> By dragging with your left mouse button you can move and position the player's name. This location will not be saved when you restart the program!
    
  3. Assign field position
    -> By dragging on a player with your 'LEFT' mouse button you can drag a new 'ghost' location to where the player must march to.
  
There are also a couple buttons on the page that are very self explanatory but I will give a breif description of what they do:
  
  1. Add Set or '+'
    --> This button create a new set to be able to edit.
    
  2. Minus Set or '-'
    --> This button takes away a set and will delete ALL information inside the set.
  
  3. 'Play' and 'Play All'
    --> The 'Play' button will play the current set. The 'Play All' button will play all the sets starting from the first set and all the way to the last set. The program will skip ALL SETS WITH NO ASSIGN BEATS.
