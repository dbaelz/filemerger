NAME=Ausarbeitung

LATEX=xelatex
BIBTEX=bibtex

SRC=${NAME}.tex BIB.bib

.PHONY: all clean

${NAME}.pdf: ${SRC}
	${LATEX} ${NAME}
	${BIBTEX} ${NAME}
	${LATEX} ${NAME}
	${LATEX} ${NAME}
	
clean:
	rm -f *.aux *.bbl *.blg *.log *.out *.pdf *.toc
