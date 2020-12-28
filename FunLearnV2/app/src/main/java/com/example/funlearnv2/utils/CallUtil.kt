package com.example.funlearnv2.utils

/*fun Fragment.call(number: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$number")
    val dialog = MaterialAlertDialogBuilder(this.requireContext())
        .setTitle("Call")
        .setMessage("Do you want to make a Call?")
        .setPositiveButton("Call") { _, _ -> startActivity(intent) }
        .setNegativeButton("Cancel") { _, _ -> }
    if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) dialog.show()
    else requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), Constants.PERMISSION_REQUEST_CALL)
}*/

/*fun Fragment.handleCallPermissionResult(requestCode: Int, grantResults: IntArray, number: String) {
    if (requestCode == Constants.PERMISSION_REQUEST_CALL && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && number.isNotEmpty())
        call(number)
}*/
