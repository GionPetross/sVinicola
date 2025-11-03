document.addEventListener("DOMContentLoaded", function() {

	// --- Gestione Cambio Tema ---
	const themeToggleBtn = document.getElementById("theme-toggle-btn");
	
	themeToggleBtn.addEventListener("click", function() {
		const htmlTag = document.documentElement; 
		let currentTheme = htmlTag.getAttribute("data-theme");
		
		if (currentTheme === "dark") {
			htmlTag.setAttribute("data-theme", "light");
		} else {
			htmlTag.setAttribute("data-theme", "dark");
		}
	});
	

	// --- Gestione Apertura/Chiusura Sidebar ---
	const sidebarToggleBtn = document.getElementById("sidebar-toggle-btn");
	const sidebarCloseBtn = document.getElementById("sidebar-close-btn");
	const sidebar = document.querySelector(".sidebar");
		
	if (sidebarToggleBtn && sidebar) {
		sidebarToggleBtn.addEventListener("click", function() {
			sidebar.classList.add("open");
		});
	}
		
	if (sidebarCloseBtn && sidebar) {
		sidebarCloseBtn.addEventListener("click", function() {
			sidebar.classList.remove("open");
		});
	}

	
	// --- Creazione Bollicine ---
	function createBubbles() {
		const canvas = document.getElementById("bubble-canvas");
		if (!canvas) return; 
		
		const bubbleCount = 100;
	
		for (let i = 0; i < bubbleCount; i++) {
			const bubble = document.createElement("div");
			bubble.classList.add("bubble");
			
			const size = Math.random() * (60 - 10) + 10;
			bubble.style.width = `${size}px`;
			bubble.style.height = `${size}px`;
			
			bubble.style.left = `${Math.random() * 100}vw`;
			
			const duration = Math.random() * (20 - 8) + 8;
			bubble.style.animationDuration = `${duration}s`;
			
			const delay = Math.random() * 5; 
			bubble.style.animationDelay = `${delay}s`;
			
			const horizontalDrift = (Math.random() - 0.5) * 2 * 150;
			bubble.style.setProperty('--horizontal-drift', `${horizontalDrift}px`);
			
			canvas.appendChild(bubble);
		}
	}
	createBubbles();


	// --- Gestione Slider Banner (Odioso) ---
	const scrollContainer = document.querySelector(".banner-scroll-container");
	const btnLeft = document.getElementById("banner-scroll-left");
	const btnRight = document.getElementById("banner-scroll-right");

	if (scrollContainer && btnLeft && btnRight) {
			
		const bannerCards = document.querySelectorAll(".banner-card");
		let currentIndex = 0;
		const totalBanners = bannerCards.length;
		let isAnimating = false; //state :)
		
		if (totalBanners === 0) {
			btnLeft.style.display = 'none';
			btnRight.style.display = 'none';
			return;
		}
		
		//fake scroll con una immagine
		if (totalBanners === 1) {
		  const singleCard = bannerCards[0];
		  singleCard.classList.add("active");

		  const loopEffect = () => {
		    if (isAnimating) return;
		    isAnimating = true;
		    singleCard.style.transition = "transform 0.4s ease-in-out, opacity 0.4s ease-in-out";
		    singleCard.style.transform = "scale(0.98)";
		    singleCard.style.opacity = "0.7";

		    setTimeout(() => {
		      singleCard.style.transform = "scale(1)";
		      singleCard.style.opacity = "1";
		      isAnimating = false;
		    }, 400);
		  };

		  btnLeft.addEventListener("click", loopEffect);
		  btnRight.addEventListener("click", loopEffect);
		  return;
		}
		
		//scroll con due immagini
		if (totalBanners === 2) {
		  bannerCards[0].classList.add('active');

		  const slide = (direction) => {
		    if (isAnimating) return;
		    isAnimating = true;

		    const current = bannerCards[currentIndex];
		    const nextIndex = direction === 'right'
		      ? (currentIndex + 1) % 2
		      : (currentIndex - 1 + 2) % 2;
		    const next = bannerCards[nextIndex];

		    next.classList.add('active');
		    current.classList.remove('active');
		    current.classList.add(direction === 'right' ? 'exit-left' : 'exit-right');

		    setTimeout(() => {
		      current.classList.remove('exit-left', 'exit-right');
		      isAnimating = false;
		    }, 600);

		    currentIndex = nextIndex;
		  };

		  btnRight.addEventListener("click", () => slide('right'));
		  btnLeft.addEventListener("click", () => slide('left'));
		  return;
		}
		
		//scroll con più immagini
		bannerCards.forEach((card, index) => {
			if (index === 0) {
				card.classList.add('active'); 
			} else {
				card.style.transform = 'translateX(100%)'; 
			}
		});


		btnRight.addEventListener("click", function() {
			if (isAnimating) return; 
			isAnimating = true;

			let currentCard = bannerCards[currentIndex];
			let nextIndex = (currentIndex + 1) % totalBanners; // Loop
			let nextCard = bannerCards[nextIndex];

			nextCard.classList.add('active'); 
			currentCard.classList.remove('active');
			currentCard.classList.add('exit-left');
			currentIndex = nextIndex;
			
			//timeout
			setTimeout(() => {
				currentCard.classList.remove('exit-left');
				isAnimating = false;
			}, 600);
		});

		btnLeft.addEventListener("click", function() {
			if (isAnimating) return;
			isAnimating = true;

			let currentCard = bannerCards[currentIndex];
			let prevIndex = (currentIndex - 1 + totalBanners) % totalBanners; // Loop
			let prevCard = bannerCards[prevIndex];
			
			currentCard.classList.remove('active');
			currentCard.classList.add('exit-right');
			prevCard.classList.add('enter-left');
			void prevCard.offsetWidth;
			prevCard.classList.add('active');
			prevCard.classList.remove('enter-left');
			
			currentIndex = prevIndex;

			//timeout
			setTimeout(() => {
				currentCard.classList.remove('exit-right');
				isAnimating = false;
			}, 600);
		});
	}
});