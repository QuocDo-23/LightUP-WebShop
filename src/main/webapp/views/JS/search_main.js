const NavSearch = {
    init() {
        this.searchInput = document.getElementById('searchInput');
        this.suggestionsDiv = document.getElementById('searchSuggestions');
        this.searchTimeout = null;

        if (this.searchInput) {
            this.searchInput.addEventListener('input', (e) => this.handleInput(e));
        }

        document.addEventListener('click', (e) => this.handleClickOutside(e));
    },

    handleInput(e) {
        clearTimeout(this.searchTimeout);

        const value = e.target.value.trim();

        if (value.length < 2) {
            this.suggestionsDiv.style.display = 'none';
            return;
        }

        this.searchTimeout = setTimeout(() => {
            this.fetchSuggestions(value);
        }, 300);
    },

    fetchSuggestions(query) {
        fetch(`search-suggestions?q=${encodeURIComponent(query)}`)
            .then(response => response.text())
            .then(html => {
                this.suggestionsDiv.innerHTML = html;
                this.suggestionsDiv.style.display = 'block';
            })
            .catch(error => console.error('Search error:', error));
    },

    handleClickOutside(e) {
        const searchIcon = document.querySelector('.search-icon');
        if (searchIcon && !searchIcon.contains(e.target)) {
            this.suggestionsDiv.style.display = 'none';
        }
    }
};

// Khởi chạy khi DOM loaded
document.addEventListener('DOMContentLoaded', () => NavSearch.init());