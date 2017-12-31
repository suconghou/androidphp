package main

import (
	"net/http"
	"os"
	"path/filepath"
)

const (
	doc     = "/mnt/sdcard/external_sd"
	index   = "index.html"
	address = ":9090"
)

func main() {
	http.HandleFunc("/", routeMatch)
	http.ListenAndServe(address, nil)
}

func routeMatch(w http.ResponseWriter, r *http.Request) {
	files := []string{index}
	if r.URL.Path != "/" {
		files = []string{r.URL.Path, filepath.Join(r.URL.Path, index)}
	}
	if !tryFiles(files, w, r) {
		http.NotFound(w, r)
	}
}

func tryFiles(files []string, w http.ResponseWriter, r *http.Request) bool {
	for _, file := range files {
		realpath := filepath.Join(doc, file)
		if f, err := os.Stat(realpath); err == nil {
			if f.Mode().IsRegular() {
				http.ServeFile(w, r, realpath)
				return true
			}
		}
	}
	return false
}
