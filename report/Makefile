MAIN = main
NOME  = Relatorio_LI4

LATEX = lualatex 

FLAGS = -interaction=nonstopmode -halt-on-error

all:
	$(LATEX) $(FLAGS) -jobname=$(NOME) $(MAIN).tex
	$(LATEX) $(FLAGS) -jobname=$(NOME) $(MAIN).tex

clean:
	rm -f *.aux *.log *.out *.toc *.lof *.lot *.synctex.gz

distclean: clean
	rm -f $(NOME).pdf

.PHONY: all clean distclean
