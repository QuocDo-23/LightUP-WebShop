document.getElementById('searchInput').addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        document.getElementById('searchForm').submit();
    }
});

function clearSearch() {
    const input = document.getElementById('searchInput');
    input.value = '';
    input.focus();
    if (new URLSearchParams(window.location.search).get('keyword')) {
        document.getElementById('searchForm').submit();
    }
}