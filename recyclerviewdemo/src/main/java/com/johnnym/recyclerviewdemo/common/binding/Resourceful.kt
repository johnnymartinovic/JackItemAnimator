package com.johnnym.recyclerviewdemo.common.binding

import android.content.res.Resources

internal fun resourceNotFound(id: Int): Nothing =
        throw Resources.NotFoundException("Resource with ID: $id not found.")
