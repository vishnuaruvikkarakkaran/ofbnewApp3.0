function TokenGenerator() {
  this.tokens = []
  this.count = 20;
  this.uid = Date.now();
}

TokenGenerator.prototype.fetchTokens = function () {
  if(this.tokens.length < (this.count / 2)) {
    var httpRequest = new XMLHttpRequest();
    var _this = this;
    const BASEURL='https://testing-neyyar.enfinlabs.com';

    if (!httpRequest) {
      console.log('Giving up :( Cannot create an XMLHTTP instance');
      return false;
    }
    httpRequest.onreadystatechange = function() {
      try {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
          if (httpRequest.status === 200) {
            var result = JSON.parse(httpRequest.responseText);
            if(result.success) {
              _this.tokens = result.data.tokens.concat(_this.tokens);
            }
          } else {
            console.log('There was a problem with the token request.');
          }
        }
      } catch(e) {
        console.log('There was a problem with the token request.');
      }
    };
    httpRequest.open('GET', BASEURL+'/get-tokens?uid='+this.uid);
    httpRequest.send();
  }
}

var TGN = new TokenGenerator();