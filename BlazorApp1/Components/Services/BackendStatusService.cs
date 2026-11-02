namespace BlazorApp1.Components.Services;

public class BackendStatusService
{
    private readonly HttpClient _http;

    public bool ModoMemoriaRAM { get; private set; } = true;
    public bool BackendEnLinea { get; private set; } = false;
    public string UltimoMensaje { get; private set; } = "Modo Local Activo";

    public event Action? OnChange;

    public BackendStatusService(IHttpClientFactory httpClientFactory)
    {
        _http = httpClientFactory.CreateClient("SpringBoot");
    }

    public void ActivarModoRAM()
    {
        ModoMemoriaRAM = true;
        BackendEnLinea = false;
        UltimoMensaje = "Modo Local Activo";
        NotifyStateChanged();
    }

    public async Task<bool> ProbarConexionBackendAsync()
    {
        try
        {
            using var cts = new CancellationTokenSource(TimeSpan.FromMilliseconds(1000));
            var res = await _http.GetAsync("management/health", cts.Token);
            if (res.IsSuccessStatusCode)
            {
                BackendEnLinea = true;
                ModoMemoriaRAM = false;
                UltimoMensaje = "Conectado al Servidor API";
                NotifyStateChanged();
                return true;
            }
        }
        catch
        {
            BackendEnLinea = false;
            ModoMemoriaRAM = true;
            UltimoMensaje = "Servidor no disponible. Continuando en modo local.";
            NotifyStateChanged();
        }

        return false;
    }

    private void NotifyStateChanged() => OnChange?.Invoke();
}
