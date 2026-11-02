using BlazorApp1.Components;
using BlazorApp1.Components.Services;

var builder = WebApplication.CreateBuilder(args);

// Servicios de componentes y render interactivo
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Cliente HTTP configurado para la API de Spring Boot
builder.Services.AddHttpClient("SpringBoot", client =>
{
    client.BaseAddress = new Uri("http://localhost:9999/");
    client.Timeout = TimeSpan.FromSeconds(3);
});

builder.Services.AddScoped(sp => sp.GetRequiredService<IHttpClientFactory>().CreateClient("SpringBoot"));

// Inyección de dependencias de negocio y estado
builder.Services.AddScoped<BackendStatusService>();
builder.Services.AddScoped<TokenService>();
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<LibroService>();
builder.Services.AddScoped<VentaService>();
builder.Services.AddScoped<CarritoService>();
builder.Services.AddScoped<LicenciaDigitalService>();
builder.Services.AddScoped<PromocionService>();
builder.Services.AddScoped<EditorialService>();
builder.Services.AddScoped<CalificacionService>();

var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();

app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
