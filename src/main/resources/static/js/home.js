document.addEventListener('DOMContentLoaded', function() {

    // ============================================
    // CARREGAMENTO E ANIMAÇÃO DO SVG
    // ============================================

    fetch('/img/fundo_jardim_169.svg')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar SVG');
            }
            return response.text();
        })
        .then(svgText => {
            const container = document.querySelector('.image-container');
            const img = container.querySelector('.bg-img');
            const heroContent = container.querySelector('.hero-content');

            // Remove a tag <img>
            if (img) {
                img.remove();
            }

            // Insere o SVG mantendo o hero-content
            container.innerHTML = svgText;
            if (heroContent) {
                container.appendChild(heroContent);
            }

            // Pega o elemento SVG que foi inserido
            const svg = container.querySelector('svg');
            if (svg) {
                // Garante que o SVG preencha o container
                svg.style.width = '100%';
                svg.style.height = '100%';
                svg.style.position = 'absolute';
                svg.style.top = '0';
                svg.style.left = '0';

                // ============================================
                // ANIMAÇÕES DOS ELEMENTOS
                // ============================================

                // g2 - Estrelas brilhando
                const estrelas = svg.querySelector('#g2');
                if (estrelas) {
                    estrelas.style.animation = 'brilhar 2s ease-in-out infinite';
                    console.log('✨ Estrelas animadas');
                }

                // g5 - Passarinho pulando
                const passaro = svg.querySelector('#g5');
                if (passaro) {
                    passaro.style.animation = 'pular 1.5s ease-in-out infinite';
                    passaro.style.transformOrigin = 'center bottom';
                    console.log('🐦 Passarinho animado');
                }

                // g8 - Flores balançando
                const flores = svg.querySelector('#g8');
                if (flores) {
                    flores.style.animation = 'balancar 4s ease-in-out infinite';
                    console.log('🌸 Flores animadas');
                }

                // g197 - Nuvens flutuando
                const nuvens = svg.querySelector('#g197');
                if (nuvens) {
                    nuvens.style.animation = 'flutuar 8s ease-in-out infinite';
                    console.log('☁️ Nuvens animadas');
                }

                console.log('SVG carregado com sucesso!');
            }
        })
        .catch(error => {
            console.error('Erro ao carregar SVG:', error);
            // Mantém a imagem original se der erro
        });

});