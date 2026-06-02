export interface DatosLogin {
  username: string;
  password: string;
}

export interface DatosRegistroUsuario {
  username: string;
  password: string;
  role?: string;
}

export interface DatosRespuestaAuth {
  token: string;
  username: string;
  role: string;
  tipo: string;
}
