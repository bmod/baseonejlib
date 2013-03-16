import bpy
import os

exportfilename = '%s.dae' % os.path.splitext(bpy.data.filepath)[0]

#print(dir(bpy.types.WM_OT_collada_export.selected))
bpy.ops.wm.collada_export(exportfilename, None)

