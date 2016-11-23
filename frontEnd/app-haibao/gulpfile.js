var gulp = require("gulp"),
	gulpLoadPlugins = require('gulp-load-plugins'),
    Browsersync = require('browser-sync').create(),
    reload = Browsersync.reload;
const $ =  gulpLoadPlugins();
gulp.task("less", function() {
	return gulp.src("./components/*/*.less")
			   .pipe($.less())
               .pipe($.autoprefixer())
			   .pipe(gulp.dest("./components/style"))
			   .pipe(reload({stream: true}));
});
gulp.task('server', ['less'], function() {
    Browsersync.init({
        server: {
            baseDir: "./"
        }
    });
    gulp.watch("./components/*/*.less", ['less']);
    gulp.watch("./components/*/*.html").on("change", reload);
});