import bpy
import os

exportfilename = '%s.dae' % os.path.splitext(bpy.data.filepath)[0]
print("Exporting %s" % exportfilename)
 
bpy.ops.wm.collada_export(filepath=exportfilename)

