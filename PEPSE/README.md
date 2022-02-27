# PEPSE: Precise Environmental Procedural Simulator Extraordinaire
A Java implementation of a simulator, which simulates reality life.

## Authors
Adam Shtrasner

Noam Alterman

## Explanation of the creation of the endless world 

We created a hash map where the key is an x coordinate and the value is ArrayList
of GameObject. For the terrain, each value corresponds to columns of blocks,
for the Tree - the trunk blocks, and the Leaf - all the leafs that correspond to the tree
in the given x coordinate.
Then, according to the avatar's current location, we got the distance that we want to add from one
side of the screen, and to remove from the other side. We used createInRange and removeInRange methods:
in createInRange we first checked whether the key already exists or not. If it doesn't exist we added
the key, and if it does we skipped it (because that means it's already on the screen),
and in removeInRange we first checked whether the key exists, if it does - we went through the
array list and removed the gameobjects from the gameObjects collection and then removed
the pair from the hash map.


## Explanation of the trees module 

The trees module consists of two classes: Tree and Leaf.
We chose to divide the module into 2 different classes
because the tree consists of two different objects which behave in
a different manner: the base tree block, that is just a static stack
of brown blocks, and the leaves on top of that base, which behave entirely
different than the base block: the leaves are a bunch of green
blocks created inside a square, and need to move, drop out and even
disappear.
The Tree class is responsible for the creation of trees in the game,
by applying its only method "createInRange". In that method, the tree
base block is created and on top of that base - the leaves.
Since it's most reasonable for the leaf to be a block, we chose it to
inherit from Block, and also so that we could define the collision
between the leaves when they fall, and the terrain, and we can do
that easily if Leaf extended GameObject, or some class that extends GameObject,
by overriding the OnCollisionEnter method.
