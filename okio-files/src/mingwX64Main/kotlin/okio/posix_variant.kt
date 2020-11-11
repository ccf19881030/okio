/*
 * Copyright (C) 2020 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okio

import kotlinx.cinterop.ByteVarOf
import kotlinx.cinterop.CPointer
import platform.posix.EACCES
import platform.posix.PATH_MAX
import platform.posix.errno
import platform.posix.getcwd
import platform.posix.mkdir
import platform.posix.remove
import platform.posix.rmdir

internal actual val VARIANT_DIRECTORY_SEPARATOR = "\\"

internal actual fun PosixSystemFilesystem.variantCanonicalize(path: Path): Path {
  throw IOException("TODO")
}

internal actual fun PosixSystemFilesystem.variantDelete(path: Path) {
  val pathString = path.toString()

  if (remove(pathString) == 0) return

  // If remove failed with EACCES, it might be a directory. Try that.
  if (errno == EACCES) {
    if (rmdir(pathString) == 0) return
  }

  throw IOException(errnoString(EACCES))
}

internal actual fun PosixSystemFilesystem.variantMkdir(dir: Path): Int {
  return mkdir(dir.toString())
}

internal actual fun PosixSystemFilesystem.variantGetCwd(): CPointer<ByteVarOf<Byte>>? {
  return getcwd(null, PATH_MAX.toInt())
}
