(function() {

    $(function() {
        $('body').geopattern(new Date().toISOString(), {
            generator: 'squares'
        });
    });

})();
