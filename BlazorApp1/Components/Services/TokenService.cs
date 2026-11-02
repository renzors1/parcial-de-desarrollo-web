namespace BlazorApp1.Components.Services;

public class TokenService
{
    private string? _token;
    private UserAccountDto? _currentUser;

    public event Action? OnChange;

    public string? Token
    {
        get => _token;
        set
        {
            _token = value;
            NotifyStateChanged();
        }
    }

    public UserAccountDto? CurrentUser
    {
        get => _currentUser;
        set
        {
            _currentUser = value;
            NotifyStateChanged();
        }
    }

    public bool IsAuthenticated => !string.IsNullOrWhiteSpace(_token);
    public bool IsAdmin => _currentUser?.IsAdmin ?? false;

    public void SetSession(string token, UserAccountDto user)
    {
        _token = token;
        _currentUser = user;
        NotifyStateChanged();
    }

    public void Clear()
    {
        _token = null;
        _currentUser = null;
        NotifyStateChanged();
    }

    public void NotifyStateChanged() => OnChange?.Invoke();
}
