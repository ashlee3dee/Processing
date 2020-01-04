import java.util.Random.choice;
import java.util.Random.sample;
import java.util.Collections.deque;
import java.io.File;

int w, h, s;
directions[][] = [(-1, 0), (1, 0), (0, -1), [0, 1]];

def setup() {
  w =80;
  h=45;
  s=10;
  size(80, 45, P2D);
  background("#FFFFFF");
  frameRate(1000);

  global W, A, H, tiles

    path = File("\Knots").listFiles()
    connectors = [(0, 1, 1, 0), (0, 0, 0, 0), (1, 0, 1, 0)]

    tiles = []
    count = 0

    // Iterating through a folder of unique tiles
    for i, png in enumerate(path):

  // appending tile to 'tiles' array lit
  tiles.append(loadImage(png.getAbsolutePath()))

    // if rotation information included in tile name --> allow rotation
    name = png.getName().split('.')[0]
  if '-' in name:

  // rotate by 90° the number of times specified in tile name 
  for rot in xrange(int(name[-1])):
  cp = tiles[-1]
    pg = createGraphics(cp.width, cp.height)
    pg.beginDraw()
    pg.pushMatrix()
    pg.translate(cp.width, 0)
    pg.rotate(HALF_PI)
    pg.image(cp, 0, 0)
    pg.popMatrix()
    pg.endDraw()

    // add rotated tile to 'tiles' array list
    tiles.append(pg)

    // create a corresponding "connector" array by rotating the indices of the previous tile
    c = deque(connectors[i + count])
    c.rotate(1)
    count += 1
    connectors.insert(i + count, tuple(c))


    ntiles = len(tiles) 

    W = dict(enumerate(tuple(set(range(ntiles)) for i in xrange(w*h)))) 
  H = dict(enumerate(sample(tuple(ntiles if i > 0 else ntiles-1 for i in xrange(w*h)), w*h)))
  A = dict(enumerate([[set() for dir in xrange(4)] for i in xrange(ntiles)]))

  for i1 in xrange(ntiles):
for i2 in xrange(ntiles):
if connectors[i1][0] == connectors[i2][2]:
A[i1][0].add(i2)
  A[i2][1].add(i1)

  if connectors[i1][1] == connectors[i2][3]: 
A[i1][2].add(i2)
  A[i2][3].add(i1)
}

def draw() {
  global H, W

  if not H:
  print ("finished")
    noLoop()
    return

    emin = min(H, key = H.get) 
    id = choice(list(W[emin]))
    W[emin] = {id}
    del H[emin]

    stack = {emin}
  while stack:
  idC = stack.pop() 
    for dir, t in enumerate(directions):
  x = (idC%w + t[0])%w
    y = (idC/w + t[1])%h
    idN = x + y * w 
  if idN in H: 
  possible = {n for idP in W[idC] for n in A[idP][dir]}
    if not W[idN].issubset(possible):
  intersection = possible & W[idN] 

  if not intersection:
  print "contradiction"
    noLoop()
    break

    W[idN] = intersection
    H[idN] = len(W[idN]) - random(.1)
    stack.add(idN)

    image(tiles[id], (emin%w) * s, (emin/w) * s, s, s)
}
