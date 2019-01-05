var barcodeScanned = document.getElementById('checkout');

function checkoutItem() {
  var barcode = barcodeScanned.value.replace(/\W/g, '');
  console.log(barcode);
  decrementOne(barcode);
}

barcodeScanned.addEventListener('keypress', function(e){
  if (e.keyCode == 13) {
    console.log('You pressed a "enter" key in somewhere');
    checkoutItem();
    barcodeScanned.value = "";
  }
});
